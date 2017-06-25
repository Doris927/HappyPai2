package com.example.tammy.happypai2.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by tammy on 17/6/5.
 */

public class ImageLoader {

    private static ImageLoader mInstance;
    /**
     * image cache
     */
    private LruCache<String, Bitmap> mLruCache;

    private ExecutorService mThreadPool;
    private static final int DEFAULT_THREAD_COUNT=1;
    /**
     * 队列的调度方式
     */
    private Type mType = Type.LIFO;
    /**
     * 任务队列
     */
    private LinkedList<Runnable> mTaskQueue;
    /**
     * 后台轮询线程
     */
    private Thread mPoolThread;
    private Handler mPoolThreadHandler;

    /**
     * UI Thread handler
     */
    private Handler mUIHandler;

    private Semaphore mSemaphorePoolThreadHandler = new Semaphore(0);

    private Semaphore mSemaphorePoolThread;


    public enum Type{
        FIFO,LIFO;
    }

    private ImageLoader(int mThreadCount, Type type){
        init(mThreadCount,type);
    }

    private void init(int mThreadCount, Type type){
        //后台轮询线程
        mPoolThread = new Thread(){
            @Override
            public void run(){
                Looper.prepare();
                mPoolThreadHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        //线程池取出一个
                        mThreadPool.execute(getTask());
                        try {
                            mSemaphorePoolThread.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }


                };
                //释放一个信号量
                mSemaphorePoolThreadHandler.release();
                Looper.loop();
            }
        };

        mPoolThread.start();

        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory/8;
        mLruCache  = new LruCache<String, Bitmap>(cacheMemory){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes()*value.getHeight();
            }
        };

        //创建线程池
        mThreadPool = Executors.newFixedThreadPool(mThreadCount);
        mTaskQueue = new LinkedList<Runnable>();
        mType = type;

        mSemaphorePoolThread = new Semaphore(mThreadCount);
    }

    /**
     * 从任务队列中取出一个方法
     * @return
     */
    private Runnable getTask() {
        if(mType == Type.FIFO){
            return mTaskQueue.removeFirst();
        }else if(mType == Type.LIFO){
            return mTaskQueue.removeLast();
        }
        return null;
    }

    public static ImageLoader getInstance(int mThreadCount, Type type){
        if(mInstance == null){
            synchronized (ImageLoader.class){
                if (mInstance == null){
                    mInstance = new ImageLoader(mThreadCount, type);
                }
            }
        }

        return mInstance;
    }

    /**
     * 根据path获得图片
     * @param path
     * @param imageView
     */
    public void loadImage(final String path, final ImageView imageView){
        imageView.setTag(path);
        if(mUIHandler==null){
            mUIHandler=new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    //获取图片
                    ImgBeanHolder holder = (ImgBeanHolder) msg.obj;
                    Bitmap bm = holder.bitmap;
                    ImageView imageview = holder.imageView;
                    String path = holder.path;

                    //将path与tag的存储路径进行绑定
                    if (imageview.getTag().toString().equals(path)){
                        imageview.setImageBitmap(bm);
                    }

                }
            };
        }
        //根据path在缓存中获得图片
        Bitmap bm = getBitmapFromLruCache(path);
        if(bm!=null){
            refreshBitmap(bm, path, imageView);
        }else {
            addTask(new Runnable(){

                @Override
                public void run() {
                    //加载图片
                    //图片压缩
                    //1.获得图片需要的大小
                    ImageSize imageSize = getImageViewSize(imageView);
                    //2.压缩图片
                    Bitmap bm = decodeSampledBitmapFromPath(path,imageSize.width, imageSize.height);

                    refreshBitmap(bm, path, imageView);

                    mSemaphorePoolThread.release();
                }
            });
        }
    }

    private void refreshBitmap(Bitmap bm, String path, ImageView imageView) {
        Message message = Message.obtain();
        ImgBeanHolder holder = new ImgBeanHolder();
        holder.bitmap = bm;
        holder.path = path;
        holder.imageView = imageView;
        message.obj = holder;
        mUIHandler.sendMessage(message);
    }


    /**
     * 根据图片需要显示的宽度和高度进行压缩
     * @param path
     * @param width
     * @param height
     * @return
     */
    protected Bitmap decodeSampledBitmapFromPath(String path, int width, int height){
        //获取图片的宽和高，并不把图片加载到内存中
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path,options);
        options.inSampleSize = caculateInSampleSize(options,width,height);

        //使用获得到的insampleSize再次解析图片
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);

        //把图片加入缓存
        addBitmapToLruCache(path,bitmap);

        return  bitmap;
    }

    /**
     * 将图片加入到缓存
     * @param path
     * @param bitmap
     */
    private void addBitmapToLruCache(String path, Bitmap bitmap) {
        if (getBitmapFromLruCache(path)==null){
            if(bitmap != null){
                mLruCache.put(path,bitmap);
            }
        }
    }

    /**
     * 根据需求的宽和高以及图片的宽和高计算samplesize
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private int caculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int width = options.outWidth;
        int height = options.outHeight;

        int inSampleSize = 1;

        if (width > reqWidth || height > reqWidth){
            int widthRadio = Math.round(width*1.0f/reqWidth);
            int heightRadio = Math.round(height*1.0f/reqHeight);
            //为了减少内存，取最小值， 可以根据自己的项目调整策略
            inSampleSize = Math.max(widthRadio, heightRadio);
        }


        return  inSampleSize;
    }

    protected ImageSize getImageViewSize(ImageView imageView) {
        ImageSize imageSize = new ImageSize();

        DisplayMetrics displayMetrics = imageView.getContext().getResources().getDisplayMetrics();

        ViewGroup.LayoutParams lp = imageView.getLayoutParams();

        int width = imageView.getWidth(); // 获取imageview在layout中的实际宽度
        if(width<=0){
            width = lp.width;//获取imageview在layout中声明的宽度
        }
        if(width<=0){
            //width = imageView.getMaxWidth(); //检查最大值
            width = getImageFieldView(imageView,"mMaxWidth");
        }
        if(width<=0){
            width = displayMetrics.widthPixels; //获取屏幕的宽度
        }

        int height = imageView.getHeight(); // 获取imageview在layout中的实际宽度
        if(height<=0){
            height = lp.height;//获取imageview在layout中声明的宽度
        }
        if(height<=0){
//            height = imageView.getMaxHeight(); //检查最大值
            height = getImageFieldView(imageView,"mMaxHeight");
        }
        if(width<=0){
            height = displayMetrics.heightPixels; //获取屏幕的宽度
        }

        imageSize.width = width;
        imageSize.height = height;


        return imageSize;
    }

    /**
     * 通过反射获取imageview的某个值
     * @param object
     * @param fieldName
     * @return
     */
    private static int getImageFieldView(Object object, String fieldName){
        int value = 0;


        try {
            Field field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = field.getInt(object);
            if (fieldValue>0 && fieldValue < Integer.MAX_VALUE){
                value = fieldValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return value;
    }

    private synchronized void addTask(Runnable runnable) {
        mTaskQueue.add(runnable);
        try {
            if(mPoolThreadHandler == null)
                mSemaphorePoolThreadHandler.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mPoolThreadHandler.sendEmptyMessage(0x110);
    }


    /**
     * 根据path在缓存中获得bitmap
     * @param key
     * @return
     */
    private Bitmap getBitmapFromLruCache(String key){
        return mLruCache.get(key);
    }

    private class ImageSize{
        int width;
        int height;
    }

    private class ImgBeanHolder{
        Bitmap bitmap;
        ImageView imageView;
        String path;
    }
}
