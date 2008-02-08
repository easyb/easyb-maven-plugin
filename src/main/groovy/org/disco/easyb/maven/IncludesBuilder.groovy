package org.disco.easyb.maven

class IncludesBuilder 
{
    String buildIncludesPattern(String path, String pattern)
    {
        String separator = System.getProperty("file.separator")
        if (!path.endsWith(separator))
            return path + separator + pattern
        else
            return path + pattern    
    }
}