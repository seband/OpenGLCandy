#version 150 core


out vec4 out_Color;
in vec2 c;
in vec2 out_TexCoord;
in vec3 out_light_pos;



uniform sampler2D texUnit;
uniform sampler2D fxMap;
uniform mat4 modelView;
uniform mat4 projection;

void main(void) {
    vec4 tex = texture(fxMap, out_TexCoord);
    vec3 lightPositionOnScreen = out_light_pos / out_light_pos.z;
     vec2 texCoord = out_TexCoord;
     float decay=0.96815;
     float exposure=0.1;
     float density=0.926;
     float weight=0.58767;

     /// NUM_SAMPLES will describe the rays quality, you can play with
     int NUM_SAMPLES = 300;
    // lightPositionOnScreen = vec3(0.5f, 0.5f,1);
     vec2 tc = out_TexCoord.xy;
     vec2 deltaTexCoord = tc;
     deltaTexCoord-= lightPositionOnScreen.xy;

     deltaTexCoord *= 1.0 / float(NUM_SAMPLES) * density;
     float illuminationDecay = 1.0;

     vec4 color =texture2D(texUnit, tc.xy)*0.4;

     for(int i=0; i < NUM_SAMPLES ; i++)
     {
        tc -= deltaTexCoord;
        vec4 s = texture2D(texUnit, tc)*0.4;
        s *= illuminationDecay * weight;
        color += s;
        illuminationDecay *= decay;
     }


    out_Color = color * exposure + tex * 1.1f;

   // out_Color = vec4(texture(depthTexture, out_TexCoord).rgb, 1.0);
}

