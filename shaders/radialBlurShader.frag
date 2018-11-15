#version 150 core


out vec4 out_Color;
in vec2 out_TexCoord;

uniform sampler2D texUnit;
uniform sampler2D depthTexture;

void main(void) {

    out_Color = vec4(texture(texUnit, out_TexCoord).rgb, 1.0);
}

