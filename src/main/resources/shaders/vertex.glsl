#version 460
layout(location = 0) in vec3 vPos;
void main() {
    gl_Position.xyz = vPos;
    gl_Position.w = 1.0;
}
