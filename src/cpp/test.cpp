#include "tensor.h"
#include <iostream>

int main(){
    IntTensor A = IntTensor({1,2,3,4},{2,2});
    IntTensor B = IntTensor({1,2,3,4},{2,2});

    IntTensor C = A+B;

    IntTensor D = 2*C;

    std::cout << (A+B).access({1,1}) << std::endl;

    for(int i:C.components){
        std::cout << i << std::endl;
    }

    for(int i:D.components){
        std::cout << i << std::endl;;
    }

    return 0;
}