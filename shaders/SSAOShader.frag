#version 150

out vec4 out_Color;
in vec2 out_TexCoord;

uniform sampler2D depthMap;

float calculateDepth(in float d, in float d1, in float d2){
  const float maxAODepth = 5.0f;
  float dav = (d1+d2)/2.0f;
  float x = d - dav;
  float dao = max(1.0f/a/a*x*(2.0f*a-x), 0.0f);
  return dao;
}
void main(void){
   int samples = 100;
   float weight = 1.0f;
   float dx = 1.0f;
   float dy = 1.0f;
   float d1, d2;
   for(int i = 0; i<samples; i++){
     d = texture(depthMap, out_TexCoord);
     d1 = texture(depthMap, vec2(out_TexCoord.x + dx, out_TexCoord.y + dx));
     d2 = texture(depthMap, vec2(out_TexCoord.x - dx, out_TexCoord.y - dy));
     ao += weight * calculateDepth(d, d1, d2);
     weight = 1-i/(float)samples;
   }
   out_Color = vec4(vec3(ao), 1.0f);
}
