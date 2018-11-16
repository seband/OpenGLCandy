#version 150 core

in vec4 in_Position;
in vec3 in_Normal;
in vec2 in_TexCoord;
in vec3 in_Tangent;
in vec3 in_Bitangent;

uniform mat4 modelView;
uniform mat4 projection;
out vec3 out_exColor;
out vec2 out_TexCoord;
out vec3 out_light_pos;
void main(void) {
  //Passthrough texcoords
  out_TexCoord=in_TexCoord;

	//Set position
	gl_Position=vec4(in_Position.xyz, 1.0);
}
