cmake_minimum_required(VERSION 3.4.1)

set(CMAKE_VERBOSE_MAKEFILE  on)

# 设置so输出路径
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY  ${PROJECT_BINARY_DIR}/libs)

# 指定libjpeg动态库路径
set(jpeglibs "${CMAKE_SOURCE_DIR}/src/main/jniLibs")

# 导入第三方库：libjpeg.so
add_library(libjpeg SHARED IMPORTED)
set_target_properties(libjpeg PROPERTIES
         IMPORTED_LOCATION "${jpeglibs}/${ANDROID_ABI}/libjpeg.so")

set(CMAKE_CXX_FLAGS  "${CMAKE_CXX_FLAGS} -std=gnu++11 -fexceptions -frtti")

# 配置、链接动态库
add_library(
        jpegutil

        SHARED

        src/main/cpp/NativeJPEG.cpp)

# 查找NDK原生库log，android
find_library(log-lib log)
find_library(android-lib android)

# 链接所有库到jpegutil
# AndroidBitmapInfo需要库jnigraphics
target_link_libraries(jpegutil
        libjpeg
        jnigraphics
        ${log-lib}
        ${android-lib})