LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

OPENCV_LIB_TYPE        := STATIC
OPENCV_INSTALL_MODULES := on
OPENCV_CAMERA_MODULES  := off

include /home/helios/Dev/OpenCV-2.4.8.2-Tegra-sdk/sdk/native/jni/OpenCV-tegra3.mk

#LOCAL_MODULE    := mixed_sample
#LOCAL_SRC_FILES := jni_part.cpp
#LOCAL_LDLIBS +=  -llog -ldl

LOCAL_MODULE    := object_tracking
LOCAL_SRC_FILES := jni_prog_00.cpp
LOCAL_LDLIBS +=  -llog -ldl

include $(BUILD_SHARED_LIBRARY)
