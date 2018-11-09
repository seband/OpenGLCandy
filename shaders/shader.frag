#version 150 core


out vec4 out_Color;
in vec3 out_exColor;
void main(void) {
    out_Color = vec4(out_exColor.x,out_exColor.y,out_exColor.z,1);
}