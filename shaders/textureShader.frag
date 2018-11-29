#version 150 core


out vec4 out_Color;
in vec2 out_TexCoord;

uniform sampler2D texUnit;
uniform sampler2D LUT;
uniform bool renderLut;
vec3 calcLUT(in sampler2D lut, in vec3 color){
    color.rgb = clamp(color.rgb, vec3(0),vec3(1));
    vec2 lutSize = vec2(0.00390625f, 0.0625f);
    vec4 lutCoordinates = vec4(0);
    color.b *= 15.0f;
    lutCoordinates.w =  min(floor(color.b), 15.0f);
    lutCoordinates.xy = color.rg * 15.0f * lutSize + 0.5f * lutSize;
    lutCoordinates.x += lutCoordinates.w * lutSize.y;
    color.rgb = texture(lut, lutCoordinates.xy).rgb;
    return color.rgb;
}
void main(void) {
    vec2 tex = vec2(out_TexCoord.x, 1.0f - out_TexCoord.y);
    out_Color = vec4(texture(texUnit, tex).xyz, 1.0);
    if(renderLut)
        out_Color = vec4(calcLUT(LUT, out_Color.rgb),1);

}

