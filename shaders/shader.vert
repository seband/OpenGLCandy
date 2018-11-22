#version 150 core

in vec4 in_Position;
in vec3 in_Normal;
in vec2 in_TexCoord;
in vec3 in_Tangent;
in vec3 in_Bitangent;

uniform mat4 modelView;
uniform mat4 cameraMatrix;
uniform mat4 projection;
out vec3 out_exColor;
out vec2 out_TexCoord;
out mat3 out_TBN;
out vec3 surf;

void main(void) {
    //TBN-matrix for normalmapping (tangent space)
    vec3 T = normalize(vec3(cameraMatrix*modelView * vec4(in_Tangent,   0.0)));
    vec3 B = normalize(vec3(cameraMatrix*modelView * vec4(in_Bitangent, 0.0)));
    vec3 N = normalize(vec3(cameraMatrix*modelView * vec4(in_Normal,    0.0)));
    out_TBN = mat3(T, B, N);

    //Passthrough texcoords
    out_TexCoord=in_TexCoord;

    //Surface location for fragment (for light calc)
	surf = vec3(cameraMatrix*modelView * vec4(in_Position.xyz,1.0));

	//Set position
	gl_Position=projection*cameraMatrix*modelView*vec4(in_Position.xyz, 1.0);
}