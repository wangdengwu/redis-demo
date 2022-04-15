#include <stdio.h>
int main() {
 struct __attribute__((packed)) s {
    char a;
    int b;
 } st;
 printf("%lu\n", sizeof(st));
 return 0;
}