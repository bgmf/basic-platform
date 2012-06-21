<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="text"/>
  <xsl:template match="bandActionsRoot">
    <xsl:text>#;</xsl:text>
    <xsl:text>DATUM;</xsl:text>
    <xsl:text>EINGANG/ABGANG;</xsl:text>
    <xsl:text>BAND-TYP;</xsl:text>
    <xsl:text>BAHN;</xsl:text>
    <xsl:text>VORGANG;</xsl:text>
    <xsl:text>GRUND;</xsl:text>
    <xsl:text>BESTAND (ENDLOS);</xsl:text>
    <xsl:text>BESTAND (QUER)</xsl:text>
    <xsl:text>
</xsl:text>
    <xsl:for-each select="band-action">
      <xsl:value-of select="position()"/><xsl:text>;</xsl:text>
      <xsl:value-of select="@date"/><xsl:text>;</xsl:text>
      <xsl:value-of select="@amount"/><xsl:text>;</xsl:text>
      <xsl:value-of select="@type"/><xsl:text>;</xsl:text>
      <xsl:value-of select="@track-string"/><xsl:text>;</xsl:text>
      <xsl:value-of select="@cause"/><xsl:text>;</xsl:text>
      <xsl:value-of select="@comment"/><xsl:text>;</xsl:text>
      <xsl:value-of select="@full-amount-c"/><xsl:text>;</xsl:text>
      <xsl:value-of select="@full-amount-l"/>
      <xsl:text>
</xsl:text>
    </xsl:for-each>
  </xsl:template>
</xsl:stylesheet>