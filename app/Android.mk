LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := app
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_SRC_FILES := \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\arm64-v8a\libopencv_calib3d.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\arm64-v8a\libopencv_core.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\arm64-v8a\libopencv_dnn.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\arm64-v8a\libopencv_features2d.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\arm64-v8a\libopencv_flann.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\arm64-v8a\libopencv_highgui.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\arm64-v8a\libopencv_imgcodecs.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\arm64-v8a\libopencv_imgproc.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\arm64-v8a\libopencv_java3.so \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\arm64-v8a\libopencv_ml.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\arm64-v8a\libopencv_objdetect.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\arm64-v8a\libopencv_photo.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\arm64-v8a\libopencv_shape.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\arm64-v8a\libopencv_stitching.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\arm64-v8a\libopencv_superres.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\arm64-v8a\libopencv_video.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\arm64-v8a\libopencv_videoio.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\arm64-v8a\libopencv_videostab.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\armeabi\libopencv_calib3d.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\armeabi\libopencv_core.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\armeabi\libopencv_dnn.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\armeabi\libopencv_features2d.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\armeabi\libopencv_flann.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\armeabi\libopencv_highgui.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\armeabi\libopencv_imgcodecs.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\armeabi\libopencv_imgproc.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\armeabi\libopencv_java3.so \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\armeabi\libopencv_ml.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\armeabi\libopencv_objdetect.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\armeabi\libopencv_photo.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\armeabi\libopencv_shape.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\armeabi\libopencv_stitching.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\armeabi\libopencv_superres.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\armeabi\libopencv_video.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\armeabi\libopencv_videoio.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\armeabi\libopencv_videostab.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\armeabi-v7a\libopencv_calib3d.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\armeabi-v7a\libopencv_core.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\armeabi-v7a\libopencv_dnn.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\armeabi-v7a\libopencv_features2d.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\armeabi-v7a\libopencv_flann.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\armeabi-v7a\libopencv_highgui.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\armeabi-v7a\libopencv_imgcodecs.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\armeabi-v7a\libopencv_imgproc.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\armeabi-v7a\libopencv_java3.so \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\armeabi-v7a\libopencv_ml.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\armeabi-v7a\libopencv_objdetect.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\armeabi-v7a\libopencv_photo.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\armeabi-v7a\libopencv_shape.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\armeabi-v7a\libopencv_stitching.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\armeabi-v7a\libopencv_superres.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\armeabi-v7a\libopencv_video.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\armeabi-v7a\libopencv_videoio.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\armeabi-v7a\libopencv_videostab.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\mips\libopencv_calib3d.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\mips\libopencv_core.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\mips\libopencv_dnn.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\mips\libopencv_features2d.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\mips\libopencv_flann.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\mips\libopencv_highgui.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\mips\libopencv_imgcodecs.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\mips\libopencv_imgproc.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\mips\libopencv_java3.so \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\mips\libopencv_ml.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\mips\libopencv_objdetect.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\mips\libopencv_photo.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\mips\libopencv_shape.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\mips\libopencv_stitching.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\mips\libopencv_superres.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\mips\libopencv_video.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\mips\libopencv_videoio.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\mips\libopencv_videostab.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\mips64\libopencv_calib3d.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\mips64\libopencv_core.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\mips64\libopencv_dnn.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\mips64\libopencv_features2d.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\mips64\libopencv_flann.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\mips64\libopencv_highgui.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\mips64\libopencv_imgcodecs.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\mips64\libopencv_imgproc.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\mips64\libopencv_java3.so \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\mips64\libopencv_ml.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\mips64\libopencv_objdetect.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\mips64\libopencv_photo.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\mips64\libopencv_shape.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\mips64\libopencv_stitching.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\mips64\libopencv_superres.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\mips64\libopencv_video.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\mips64\libopencv_videoio.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\mips64\libopencv_videostab.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\x86\libopencv_calib3d.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\x86\libopencv_core.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\x86\libopencv_dnn.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\x86\libopencv_features2d.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\x86\libopencv_flann.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\x86\libopencv_highgui.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\x86\libopencv_imgcodecs.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\x86\libopencv_imgproc.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\x86\libopencv_java3.so \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\x86\libopencv_ml.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\x86\libopencv_objdetect.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\x86\libopencv_photo.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\x86\libopencv_shape.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\x86\libopencv_stitching.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\x86\libopencv_superres.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\x86\libopencv_video.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\x86\libopencv_videoio.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\x86\libopencv_videostab.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\x86_64\libopencv_calib3d.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\x86_64\libopencv_core.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\x86_64\libopencv_dnn.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\x86_64\libopencv_features2d.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\x86_64\libopencv_flann.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\x86_64\libopencv_highgui.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\x86_64\libopencv_imgcodecs.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\x86_64\libopencv_imgproc.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\x86_64\libopencv_java3.so \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\x86_64\libopencv_ml.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\x86_64\libopencv_objdetect.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\x86_64\libopencv_photo.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\x86_64\libopencv_shape.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\x86_64\libopencv_stitching.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\x86_64\libopencv_superres.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\x86_64\libopencv_video.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\x86_64\libopencv_videoio.a \
	D:\androidworkspace\OpencvCamera\app\src\main\jniLibs\x86_64\libopencv_videostab.a \

LOCAL_C_INCLUDES += D:\androidworkspace\OpencvCamera\app\src\debug\jni
LOCAL_C_INCLUDES += D:\androidworkspace\OpencvCamera\app\src\main\jniLibs
LOCAL_C_INCLUDES += D:\androidworkspace\OpencvCamera\app\src\main\jni

include $(BUILD_SHARED_LIBRARY)
