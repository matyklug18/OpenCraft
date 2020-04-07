#version 460
layout(location = 0) in vec3 vPos;

uniform mat4 project;

void main() {
    gl_Position = project * vec4(vPos, 1.0);
}
