#version 460
in vec3 passTint;
out vec4 color;
void main(){
    color = vec4(passTint,1);
}