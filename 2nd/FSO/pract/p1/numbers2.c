#include <stdio.h>

#define N 10

int Number;

int main() 
{
    int i;
    
    printf("Introduce un numero");
    scanf("%d", &Number);
    
    printf(" The first %d natural numbers are: \n", Number);
    for (i = 0; i < Number; i++)
    {
        printf("*       %d \n", i);
    }
    
    printf("BYE \n");
    return (0);
}
