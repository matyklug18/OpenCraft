#version 460
layout(location = 0) in vec3 vPos;
in vec4 tint;
in vec3 normal;
out vec4 passTint;
out float mul;
uniform mat4 project;
uniform mat4 view;
uniform mat4 model;

void main() {
    gl_Position = project * view * model * vec4(vPos, 1.0);
    passTint = tint;
    if(abs(normal.x) > 0.1)
        mul = 0.5;
    if(abs(normal.z) > 0.1)
        mul = 0.7;
    if(normal.y > 0.1)
        mul = 0.9;
    if(normal.y < -0.1)
        mul = 0.3;
}