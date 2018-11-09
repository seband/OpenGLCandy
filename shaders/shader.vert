#version 150 core

in vec4 in_Position;
in vec4 in_Normal;
uniform mat4 rotation;
out vec4 out_Normal;
void main(void) {
    out_Normal = in_Normal;
    gl_Position = rotation * in_Position;
}