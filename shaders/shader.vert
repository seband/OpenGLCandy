#version 150 core

in vec4 in_Position;
uniform mat4 rotation;

void main(void) {
    gl_Position = rotation * in_Position;
}