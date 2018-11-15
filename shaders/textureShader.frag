#version 150 core


out vec4 out_Color;
in vec2 out_TexCoord;

uniform sampler2D texUnit;

void main(void) {

    out_Color = vec4(texture(texUnit, out_TexCoord).xyz, 1.0);
}

