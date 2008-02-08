package org.disco.easyb.maven

class IncludesBuilderTest extends GroovyTestCase
{
    def builder = new IncludesBuilder()

    public void testBuildIncludesStringFromPathEndingInSlash()
    {
        String pattern = builder.buildIncludesPattern('src/main/easyb', '**/*.groovy')
        assertEquals('src/main/easyb/**/*.groovy', pattern)
    }

    public void testBuildIncludesStringFromPathNotEndingInSlash()
    {
        String pattern = builder.buildIncludesPattern('src/main/easyb/', '**/*.groovy')
        assertEquals('src/main/easyb/**/*.groovy', pattern)
    }
}