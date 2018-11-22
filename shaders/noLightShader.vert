#version 150 core

in vec4 in_Position;
in vec2 in_TexCoord;


uniform mat4 modelView;
uniform mat4 cameraMatrix;
uniform mat4 projection;
out vec2 out_TexCoord;

void main(void) {
    //Passthrough texcoords
    out_TexCoord=in_TexCoord;

	//Set position
	gl_Position=projection*cameraMatrix*modelView*vec4(in_Position.xyz, 1.0);
}