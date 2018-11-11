#version 150 core


out vec4 out_Color;
in vec3 out_exColor;
in vec2 out_TexCoord;
uniform sampler2D texUnit;
void main(void) {
	out_Color = texture(texUnit, out_TexCoord);
}