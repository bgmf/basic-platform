<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="text"/>
  <xsl:template match="personListRoot">
    <xsl:text>#;</xsl:text>
    <xsl:text>Name;</xsl:text>
    <xsl:text>Vorname;</xsl:text>
    <xsl:text>Straße;</xsl:text>
    <xsl:text>PLZ;</xsl:text>
    <xsl:text>Ort;</xsl:text>
    <xsl:text>Geburtsdatum;</xsl:text>
    <xsl:text>Telefon;</xsl:text>
    <xsl:text>Mobiltelefon;</xsl:text>
    <xsl:text>Fax;</xsl:text>
    <xsl:text>E-Mail</xsl:text>
    <xsl:text>
</xsl:text>
    <xsl:for-each select="person">
      <xsl:value-of select="position()"/><xsl:text>;</xsl:text>
      <xsl:value-of select="@name"/><xsl:text>;</xsl:text>
      <xsl:value-of select="@first-name"/><xsl:text>;</xsl:text>
      <xsl:value-of select="@street"/><xsl:text>;</xsl:text>
      <xsl:value-of select="@postal-code"/><xsl:text>;</xsl:text>
      <xsl:value-of select="@city"/><xsl:text>;</xsl:text>
      <xsl:value-of select="@birthday"/><xsl:text>;</xsl:text>
      <xsl:value-of select="@phone"/><xsl:text>;</xsl:text>
      <xsl:value-of select="@mobile"/><xsl:text>;</xsl:text>
      <xsl:value-of select="@fax"/><xsl:text>;</xsl:text>
      <xsl:value-of select="@e-mail"/>
      <xsl:text>
</xsl:text>
    </xsl:for-each>
  </xsl:template>
</xsl:stylesheet>