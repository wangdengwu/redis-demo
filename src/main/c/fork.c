#include <stdio.h>
#include <unistd.h>

int main(int argc, char **argv) {
	int i = fork();
	if(i==0){
	   printf("world\n");
	   return 0;
	}else{
	   printf("hello ");
	   return 1;
	}
}