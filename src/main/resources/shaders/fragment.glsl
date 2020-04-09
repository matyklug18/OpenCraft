#version 460
in vec4 passTint;
in float mul;
out vec4 color;
void main(){
    color = passTint/vec4(256)*vec4(vec3(mul), 1.);
}