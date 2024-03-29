.POSIX:
.SUFFIXES:
PREFIX ?= /usr/local
VPATH ?= .

# FFI auxiliary makefile script
.SUFFIXES: .s .o .a .h .c

PKG_CONFIG ?= pkg-config --with-path=$(PREFIX)/lib/pkgconfig

groupid ?= org.sandbox
pkg ?= foreignc
parent ?= intro_groovy
namespace_path ?= $(shell echo $(groupid).$(parent).$(pkg) | sed 'y|.|/|')
proj ?= $(groupid).$(parent).$(pkg)

CC ?= clang		# clang | gcc

ifeq ($(shell sh -c 'uname -s 2>/dev/null || echo not'),Darwin)
sosuffix = dylib
else
sosuffix = so
LDFLAGS := $(LDFLAGS) -Wl,--enable-new-dtags
endif

ffi_libdir = $(shell $(PKG_CONFIG) --variable=libdir intro_c-practice || echo .)
ffi_incdir = $(shell $(PKG_CONFIG) --variable=includedir intro_c-practice || echo .)
LD_LIBRARY_PATH := $(LD_LIBRARY_PATH):$(ffi_libdir)
export LD_LIBRARY_PATH

java_incdir = $(shell find $(JAVA_HOME) -type f -name 'jni.h' -exec dirname {} \;)

CPPFLAGS := $(CPPFLAGS) -I$(java_incdir) -I$(java_incdir)/linux -I$(java_incdir)/freebsd -I$(ffi_incdir)
LDFLAGS := $(LDFLAGS) -Wl,-rpath,'$$ORIGIN/:$(ffi_libdir)' -L$(ffi_libdir)
CFLAGS := $(CFLAGS) -Wall -pedantic -std=c99
ARFLAGS = rvcs
LDLIBS := $(LDLIBS) -lintro_c-practice

src_c = build/classic_c_wrap.c

build/lib$(proj)_stubs.$(sosuffix) : $(src_c)

build/%.so : 
	-$(LINK.c) -fPIC -shared $^ -o $@ $(LDLIBS)
build/%.dylib : 
	-$(LINK.c) -fPIC -dynamiclib -undefined suppress -flat_namespace $^ -o $@ $(LDLIBS)

.PHONY: auxffi
auxffi: build/lib$(proj)_stubs.a($(src_c:.c=.o)) build/lib$(proj)_stubs.$(sosuffix) ## compile FFI auxiliary products

.PHONY: prep_swig clean_swig
prep_swig build/classic_c_wrap.c : src/main/groovy/$(namespace_path)/Classic_c.i ## prepare Swig files
	-mkdir -p build/classes
	-swig -v -java -package $(groupid).$(parent).$(pkg) -I$(ffi_incdir) -outdir src/main/java/$(namespace_path)/ -o build/classic_c_wrap.c src/main/groovy/$(namespace_path)/Classic_c.i
clean_swig : ## clean Swig files
	-rm -fr src/main/java/$(namespace_path)/Classic_c*.java
