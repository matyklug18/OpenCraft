#version 460
layout(location = 0) in vec3 vPos;
in vec3 tint;
out vec3 passTint;
uniform mat4 project;
uniform mat4 view;
uniform mat4 model;

void main() {
    gl_Position = project * view * model * vec4(vPos, 1.0);
    passTint = tint;
}