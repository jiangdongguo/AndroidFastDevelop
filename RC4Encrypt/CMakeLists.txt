cmake_minimum_required(VERSION 3.4.1)

set(CMAKE_VERBOSE_MAKEFILE  on)
# 指定so输出路径
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${PROJECT_BINARY_DIR}/libs)
# 添加自定义库rc4util
add_library(
        rc4util
        SHARED
        src/main/cpp/NativeRC4.cpp
        )
# 查找系统库
find_library(log-lib log)
find_library(android-lib android)
# 链接所有库到rc4util
target_link_libraries(
        rc4util

        ${log-lib}
        ${android-lib}
)