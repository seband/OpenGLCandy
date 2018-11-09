#version 150 core


out vec4 out_Color;
in vec4 out_Normal;
void main(void) {
    out_Color = vec4(out_Normal.x,out_Normal.y,out_Normal.z,1);
}