#version 150 core


out vec4 out_Color;
in vec2 out_TexCoord;

uniform sampler2D texUnit;
uniform int isLit;

void main(void) {

    vec3 tex;
    if(isLit == 1){
        tex = vec3(1,1,1);
    }else{
        tex = vec3(0,0,0);
    }

    out_Color = vec4(tex, 1.0);
}

