#version 150 core


out vec4 out_Color;
in vec2 out_TexCoord;

uniform sampler2D texUnit;

void main(void) {
    vec2 tex = vec2(out_TexCoord.x, 1.0f - out_TexCoord.y);
    out_Color = vec4(texture(texUnit, tex).xyz, 1.0);
}

