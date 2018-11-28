#version 150 core

in vec4 in_Position;

uniform mat4 modelView;
uniform mat4 cameraMatrix;
uniform mat4 projection;


void main(void) {
	gl_Position=projection*cameraMatrix*modelView*vec4(in_Position.xyz, 1.0);
}