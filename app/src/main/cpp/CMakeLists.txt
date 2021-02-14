cmake_minimum_required(VERSION 3.4.1)

set(OBOE_DIR ../../../../oboe)
add_subdirectory (${OBOE_DIR} ./oboe)

# Pull in parselib
set (PARSELIB_DIR ../../../../parselib)
#message("PARSELIB_DIR = " + ${PARSELIB_DIR})

# Pull in iolib
set (IOLIB_DIR ../../../../iolib)
#message("IOLIB_DIR = " + ${IOLIB_DIR})

# include folders
include_directories(
        ${OBOE_DIR}/include
        ${CMAKE_CURRENT_LIST_DIR}
)

# Include the WavLib headers and shared sample code
include_directories(
        ${PARSELIB_DIR}/src/main/cpp
        ${IOLIB_DIR}/src/main/cpp)

include(${PARSELIB_DIR}/src/main/cpp/CMakeLists.txt)
include(${IOLIB_DIR}/src/main/cpp/CMakeLists.txt)

# App specific sources
set (APP_SOURCES
        NativePlayer.cpp
        )

#find_package (oboe REQUIRED CONFIG)

add_library(bomboleguero SHARED NativePlayer.cpp)

target_link_libraries(
        bomboleguero
        android
        log

        -Wl,--whole-archive
        iolib
        parselib
        -Wl,--no-whole-archive

        oboe)

# TODO removed the set() call for oboe. I don't think I'll need it. Also no bin subdirectory in oboe. These might cause problems later. Beware.


# App specific sources
#set (APP_SOURCES
#        DrumPlayerJNI.cpp
#        )


## Enable optimization flags: if having problems with source level debugging,
## disable -Ofast ( and debug ), re-enable after done debugging.
#target_compile_options(drumthumper PRIVATE -Wall -Werror "$<$<CONFIG:RELEASE>:-Ofast>")