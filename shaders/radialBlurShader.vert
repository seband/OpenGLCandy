#version 150 core

in vec4 in_Position;
in vec3 in_Normal;
in vec2 in_TexCoord;
in vec3 in_Tangent;
in vec3 in_Bitangent;

uniform mat4 modelView;
uniform mat4 projection;
uniform vec3 lightPosition;
uniform mat4 cameraMatrix;
out vec3 out_exColor;
out vec2 out_TexCoord;
out vec3 out_light_pos;
void main(void) {

    out_light_pos = (projection * cameraMatrix* modelView * vec4(lightPosition, 1.0f)).xyz;
    //Passthrough texcoords
    out_TexCoord=in_TexCoord;

	//Set position
	gl_Position=vec4(in_Position.xyz, 1.0);
}