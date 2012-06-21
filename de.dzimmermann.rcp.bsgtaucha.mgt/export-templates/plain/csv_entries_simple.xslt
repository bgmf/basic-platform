<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:math="http://www.w3.org/1998/Math/MathML" xmlns:dom="http://www.w3.org/2001/xml-events" xmlns:xforms="http://www.w3.org/2002/xforms" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="2.0">
  <xsl:output method="text"/>
  <xsl:template match="bandActionsRoot">
    <xsl:text>"#";</xsl:text>
    <xsl:text>"DATUM";</xsl:text>
    <xsl:text>"ZEIT";</xsl:text>
    <xsl:text>"PERSON";</xsl:text>
    <xsl:text>"TÄTIGKEIT";</xsl:text>
    <xsl:text>"BEMERKUNG";</xsl:text>
    <xsl:text>"BAND-TYP";</xsl:text>
    <xsl:text>"ANZAHL";</xsl:text>
    <xsl:text>"BAHN"</xsl:text>
    <xsl:text>
</xsl:text>
    <xsl:for-each select="band-action">
      <xsl:text>"</xsl:text><xsl:value-of select="position()"/><xsl:text>";</xsl:text>
      <xsl:text>"</xsl:text><xsl:value-of select="@date-string"/><xsl:text>";</xsl:text>
      <!-- <xsl:text>"</xsl:text><xsl:value-of select="format-dateTime(@date, '[D01].[M01].[Y0001]')"/><xsl:text>";</xsl:text> -->
      <xsl:text>"</xsl:text><xsl:value-of select="@time"/><xsl:text>";</xsl:text>
      <xsl:text>"</xsl:text><xsl:value-of select="person-string"/><xsl:text>";</xsl:text>
      <xsl:text>"</xsl:text><xsl:value-of select="cause"/><xsl:text>";</xsl:text>
      <xsl:text>"</xsl:text><xsl:value-of select="comment"/><xsl:text>";</xsl:text>
      <xsl:text>"</xsl:text><xsl:value-of select="@type"/><xsl:text>";</xsl:text>
      <xsl:text>"</xsl:text><xsl:value-of select="@amount"/><xsl:text>";</xsl:text>
      <xsl:text>"</xsl:text><xsl:value-of select="track-string"/><xsl:text>"</xsl:text>
      <xsl:text>
</xsl:text>
    </xsl:for-each>
  </xsl:template>
</xsl:stylesheet>