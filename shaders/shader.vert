#version 150 core

in vec4 in_Position;
in vec3 in_Normal;
uniform mat4 modelView;
uniform mat4 projection;
out vec3 out_exColor;
void main(void) {
    const vec3 light = vec3(0.78, 0.78, 0.78);
	float kd = 1;
	float ll = 0.9f;
	float la = 0.9f;
	float ldiff = kd*ll*dot(normalize(in_Normal), light)+la;
	ldiff = clamp(ldiff,0 ,1);
    out_exColor = vec3(ldiff);
    gl_Position = projection * modelView * in_Position;
}