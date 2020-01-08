cmake_minimum_required(VERSION 3.4.1)

set(SRC_DIR src/main/cpp/lame)
include_directories(src/main/cpp/lame)
aux_source_directory(src/main/cpp/lame SRC_LIST)
# 设置so动态库最后的输出路径
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI})

add_library(
             LameMp3
             SHARED
             src/main/cpp/LameMp3.c ${SRC_LIST})

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

target_link_libraries( # Specifies the target library.
                       LameMp3

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )
