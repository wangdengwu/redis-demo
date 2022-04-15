uname_S := $(shell sh -c 'uname -s 2>/dev/null || echo not')

ifeq ($(uname_S),Linux)
	SHOBJ_CFLAGS ?= -W -Wall -fno-common -g -ggdb -std=c99 -O2
	SHOBJ_LDFLAGS ?= -shared
else
	SHOBJ_CFLAGS ?= -W -Wall -dynamic -fno-common -g -ggdb -std=c99 -O2
	SHOBJ_LDFLAGS ?= -L/Library/Developer/CommandLineTools/SDKs/MacOSX.sdk/usr/lib -bundle -undefined dynamic_lookup
endif

.SUFFIXES: .c .so .xo .o

all: scankeys.so fork packed unpacked

.c.xo:
	$(CC) -I. $(CFLAGS) $(SHOBJ_CFLAGS) -fPIC -c $< -o $@

scankeys.xo: redismodule.h

scankeys.so: scankeys.xo fork.c
	$(LD) -o $@ $< $(SHOBJ_LDFLAGS) $(LIBS) -lpthread -lc

fork: fork.c
	gcc $< -o $@

packed: packed.c
	gcc $< -o $@

unpacked: unpacked.c
	gcc $< -o $@

clean:
	rm -rf *.xo *.so fork packed unpacked
