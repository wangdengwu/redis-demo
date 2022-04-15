
all: fork packed unpacked

fork: fork.c
	gcc $< -o $@

packed: packed.c
	gcc $< -o $@

unpacked: unpacked.c
	gcc $< -o $@

clean:
	rm -rf fork packed unpacked
