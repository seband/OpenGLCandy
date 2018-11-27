#version 150 core
//Godrays-shader based on the works by Nvidia (https://developer.nvidia.com/gpugems/GPUGems3/gpugems3_ch13.html)
//Values for the constants as suggested by Julien Moreau-Mathis(https://medium.com/community-play-3d/god-rays-whats-that-5a67f26aeac2)

out vec4 out_Color;
in vec2 c;
in vec2 out_TexCoord;
in vec3 out_light_pos;

uniform sampler2D texUnit;
uniform sampler2D fxMap;
uniform bool renderGodrays;

void main(void) {
    //CONSTANTS
    int NUM_SAMPLES = 100;
    float decay=0.96815;
    float exposure=0.1;
    float density=1.926;
    float weight=0.58767;
    float illuminationDecay = 1.0;

    vec4 tex = texture(fxMap, out_TexCoord);

    //Calculate light's position on screen
    vec3 lightPositionOnScreen = out_light_pos / out_light_pos.z;
    lightPositionOnScreen.x = (lightPositionOnScreen.x+1)/2;
    lightPositionOnScreen.y = (lightPositionOnScreen.y+1)/2;

    //Calculate texcoords to match sun
    vec2 tc = out_TexCoord.xy;
    vec2 deltaTexCoord = out_TexCoord.xy;
    deltaTexCoord-= lightPositionOnScreen.xy;
    deltaTexCoord *= 1.0 / float(NUM_SAMPLES) * density;

    vec4 color =texture2D(texUnit, tc.xy)*0.4;

    if(!renderGodrays){
        out_Color = color/0.4f+ tex * 1.1f;
        return;
    }

    //"Radial blur" by texture sampling
    for(int i=0; i < NUM_SAMPLES ; i++)
    {
        tc -= deltaTexCoord;
        vec4 s = texture2D(texUnit, tc)*0.4;
        s *= illuminationDecay * weight;
        color += s;
        illuminationDecay *= decay;
    }

    out_Color = color * exposure + tex * 1.1f;

}

