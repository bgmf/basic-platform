<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:template match="/">
  <!-- xmlns:target="" -->
<office:document-content xmlns:office="urn:oasis:names:tc:opendocument:xmlns:office:1.0" xmlns:style="urn:oasis:names:tc:opendocument:xmlns:style:1.0" xmlns:text="urn:oasis:names:tc:opendocument:xmlns:text:1.0" xmlns:table="urn:oasis:names:tc:opendocument:xmlns:table:1.0" xmlns:draw="urn:oasis:names:tc:opendocument:xmlns:drawing:1.0" xmlns:fo="urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:meta="urn:oasis:names:tc:opendocument:xmlns:meta:1.0" xmlns:number="urn:oasis:names:tc:opendocument:xmlns:datastyle:1.0" xmlns:presentation="urn:oasis:names:tc:opendocument:xmlns:presentation:1.0" xmlns:svg="urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0" xmlns:chart="urn:oasis:names:tc:opendocument:xmlns:chart:1.0" xmlns:dr3d="urn:oasis:names:tc:opendocument:xmlns:dr3d:1.0" xmlns:math="http://www.w3.org/1998/Math/MathML" xmlns:form="urn:oasis:names:tc:opendocument:xmlns:form:1.0" xmlns:script="urn:oasis:names:tc:opendocument:xmlns:script:1.0" xmlns:ooo="http://openoffice.org/2004/office" xmlns:ooow="http://openoffice.org/2004/writer" xmlns:oooc="http://openoffice.org/2004/calc" xmlns:dom="http://www.w3.org/2001/xml-events" xmlns:xforms="http://www.w3.org/2002/xforms" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:rpt="http://openoffice.org/2005/report" xmlns:of="urn:oasis:names:tc:opendocument:xmlns:of:1.2" xmlns:xhtml="http://www.w3.org/1999/xhtml" xmlns:grddl="http://www.w3.org/2003/g/data-view#" xmlns:tableooo="http://openoffice.org/2009/table" xmlns:field="urn:openoffice:names:experimental:ooo-ms-interop:xmlns:field:1.0" xmlns:formx="urn:openoffice:names:experimental:ooxml-odf-interop:xmlns:form:1.0" xmlns:css3t="http://www.w3.org/TR/css3-text/" office:version="1.2" grddl:transformation="http://docs.oasis-open.org/office/1.2/xslt/odf2rdf.xsl">
  <office:scripts/>
  <office:font-face-decls>
    <style:font-face style:name="Arial" svg:font-family="Arial" style:font-family-generic="swiss" style:font-pitch="variable"/>
    <style:font-face style:name="DejaVu Sans" svg:font-family="'DejaVu Sans'" style:font-family-generic="system" style:font-pitch="variable"/>
    <style:font-face style:name="Lohit Hindi" svg:font-family="'Lohit Hindi'" style:font-family-generic="system" style:font-pitch="variable"/>
    <style:font-face style:name="Mangal" svg:font-family="Mangal" style:font-family-generic="system" style:font-pitch="variable"/>
    <style:font-face style:name="Microsoft YaHei" svg:font-family="'Microsoft YaHei'" style:font-family-generic="system" style:font-pitch="variable"/>
  </office:font-face-decls>
  <office:automatic-styles>
    <style:style style:name="co1" style:family="table-column">
      <style:table-column-properties fo:break-before="auto" style:column-width="1.783cm"/>
    </style:style>
    <style:style style:name="co2" style:family="table-column">
      <style:table-column-properties fo:break-before="auto" style:column-width="1.995cm"/>
    </style:style>
    <style:style style:name="co3" style:family="table-column">
      <style:table-column-properties fo:break-before="auto" style:column-width="1.889cm"/>
    </style:style>
    <style:style style:name="co4" style:family="table-column">
      <style:table-column-properties fo:break-before="auto" style:column-width="2.267cm"/>
    </style:style>
    <style:style style:name="co5" style:family="table-column">
      <style:table-column-properties fo:break-before="auto" style:column-width="3.408cm"/>
    </style:style>
    <style:style style:name="co6" style:family="table-column">
      <style:table-column-properties fo:break-before="auto" style:column-width="4.597cm"/>
    </style:style>
    <style:style style:name="co7" style:family="table-column">
      <style:table-column-properties fo:break-before="auto" style:column-width="2.021cm"/>
    </style:style>
    <style:style style:name="ro1" style:family="table-row">
      <style:table-row-properties style:row-height="0.452cm" fo:break-before="auto" style:use-optimal-row-height="true"/>
    </style:style>
    <style:style style:name="ta1" style:family="table" style:master-page-name="Default">
      <style:table-properties table:display="true" style:writing-mode="lr-tb"/>
    </style:style>
    <number:number-style style:name="N1">
      <number:number number:decimal-places="0" number:min-integer-digits="1"/>
    </number:number-style>
    <number:date-style style:name="N37" number:automatic-order="true">
      <number:day number:style="long"/>
      <number:text>.</number:text>
      <number:month number:style="long"/>
      <number:text>.</number:text>
      <number:year/>
    </number:date-style>
    <style:style style:name="ce1" style:family="table-cell" style:parent-style-name="Default">
      <style:table-cell-properties style:text-align-source="fix" style:repeat-content="false" fo:border="0.002cm solid #000000"/>
      <style:paragraph-properties fo:text-align="center" fo:margin-left="0cm"/>
      <style:text-properties fo:font-weight="bold" style:font-weight-asian="bold" style:font-weight-complex="bold"/>
    </style:style>
    <style:style style:name="ce2" style:family="table-cell" style:parent-style-name="Default">
      <style:table-cell-properties fo:border-bottom="0.002cm solid #000000" style:text-align-source="fix" style:repeat-content="false" fo:border-left="0.002cm solid #000000" fo:border-right="none" fo:border-top="0.002cm solid #000000"/>
      <style:paragraph-properties fo:text-align="center" fo:margin-left="0cm"/>
      <style:text-properties fo:font-weight="bold" style:font-weight-asian="bold" style:font-weight-complex="bold"/>
    </style:style>
    <style:style style:name="ce3" style:family="table-cell" style:parent-style-name="Default" style:data-style-name="N37">
      <style:table-cell-properties style:text-align-source="fix" style:repeat-content="false"/>
      <style:paragraph-properties fo:text-align="center" fo:margin-left="0cm"/>
    </style:style>
    <style:style style:name="ce4" style:family="table-cell" style:parent-style-name="Default">
      <style:table-cell-properties style:text-align-source="fix" style:repeat-content="false"/>
      <style:paragraph-properties fo:text-align="center" fo:margin-left="0cm"/>
    </style:style>
    <style:style style:name="ce5" style:family="table-cell" style:parent-style-name="Default">
      <style:table-cell-properties fo:border-bottom="0.002cm solid #000000" style:text-align-source="fix" style:repeat-content="false" fo:border-left="none" fo:border-right="none" fo:border-top="0.002cm solid #000000"/>
      <style:paragraph-properties fo:text-align="center" fo:margin-left="0cm"/>
      <style:text-properties fo:font-weight="bold" style:font-weight-asian="bold" style:font-weight-complex="bold"/>
    </style:style>
    <style:style style:name="ce6" style:family="table-cell" style:parent-style-name="Default">
      <style:table-cell-properties fo:border-bottom="0.002cm solid #000000" style:text-align-source="fix" style:repeat-content="false" fo:border-left="none" fo:border-right="0.002cm solid #000000" fo:border-top="0.002cm solid #000000"/>
      <style:paragraph-properties fo:text-align="center" fo:margin-left="0cm"/>
      <style:text-properties fo:font-weight="bold" style:font-weight-asian="bold" style:font-weight-complex="bold"/>
    </style:style>
    <style:style style:name="ce7" style:family="table-cell" style:parent-style-name="Default" style:data-style-name="N1">
      <style:table-cell-properties style:text-align-source="fix" style:repeat-content="false"/>
      <style:paragraph-properties fo:text-align="center" fo:margin-left="0cm"/>
    </style:style>
    <style:style style:name="ta_extref" style:family="table">
      <style:table-properties table:display="false"/>
    </style:style>
  </office:automatic-styles>
  <office:body>
    <office:spreadsheet>
      <table:calculation-settings table:case-sensitive="false" table:use-regular-expressions="false"/>
      <table:table table:name="Tabelle1" table:style-name="ta1" table:print="false">
        <table:table-column table:style-name="co1" table:default-cell-style-name="ce3"/>
        <table:table-column table:style-name="co2" table:default-cell-style-name="ce4"/>
        <table:table-column table:style-name="co3" table:default-cell-style-name="ce4"/>
        <table:table-column table:style-name="co4" table:default-cell-style-name="ce4"/>
        <table:table-column table:style-name="co5" table:default-cell-style-name="Default"/>
        <table:table-column table:style-name="co6" table:default-cell-style-name="Default"/>
        <table:table-column table:style-name="co7" table:default-cell-style-name="ce4"/>
        <table:table-column table:style-name="co4" table:number-columns-repeated="3" table:default-cell-style-name="Default"/>
        <table:table-row table:style-name="ro1">
          <table:table-cell table:style-name="ce1" office:value-type="string" table:number-columns-spanned="10" table:number-rows-spanned="1">
            <text:p>BÄNDERBESTANDSFÜHRUNG</text:p>
          </table:table-cell>
          <table:covered-table-cell table:style-name="Default"/>
          <table:covered-table-cell table:style-name="Default"/>
          <table:covered-table-cell table:style-name="Default"/>
          <table:covered-table-cell table:number-columns-repeated="2"/>
          <table:covered-table-cell table:style-name="Default"/>
          <table:covered-table-cell table:number-columns-repeated="3"/>
        </table:table-row>
        <table:table-row table:style-name="ro1" table:number-rows-repeated="2">
          <table:table-cell table:style-name="Default" table:number-columns-repeated="4"/>
          <table:table-cell table:number-columns-repeated="2"/>
          <table:table-cell table:style-name="Default"/>
          <table:table-cell table:number-columns-repeated="3"/>
        </table:table-row>
        <table:table-row table:style-name="ro1">
          <table:table-cell table:style-name="ce2" office:value-type="string">
            <text:p>DATUM</text:p>
          </table:table-cell>
          <table:table-cell table:style-name="ce1" office:value-type="string">
            <text:p>EINGANG</text:p>
          </table:table-cell>
          <table:table-cell table:style-name="ce5" office:value-type="string">
            <text:p>ABGANG</text:p>
          </table:table-cell>
          <table:table-cell table:style-name="ce1" office:value-type="string">
            <text:p>BAHN</text:p>
          </table:table-cell>
          <table:table-cell table:style-name="ce5" office:value-type="string">
            <text:p>VORGANG</text:p>
          </table:table-cell>
          <table:table-cell table:style-name="ce1" office:value-type="string">
            <text:p>GRUND</text:p>
          </table:table-cell>
          <table:table-cell table:style-name="ce6" office:value-type="string">
            <text:p>BESTAND</text:p>
          </table:table-cell>
          <table:table-cell table:number-columns-repeated="3"/>
        </table:table-row>
        <table:table-row table:style-name="ro1">
          <table:table-cell office:value-type="date" office:date-value="2009-05-01">
            <text:p>01.05.09</text:p>
          </table:table-cell>
          <table:table-cell table:number-columns-repeated="4"/>
          <table:table-cell office:value-type="string">
            <text:p>Anfangsbestand</text:p>
          </table:table-cell>
          <table:table-cell office:value-type="float" office:value="8">
            <text:p>8</text:p>
          </table:table-cell>
          <table:table-cell table:number-columns-repeated="3"/>
        </table:table-row>
        <table:table-row table:style-name="ro1">
          <table:table-cell office:value-type="date" office:date-value="2009-05-04">
            <text:p>04.05.09</text:p>
          </table:table-cell>
          <table:table-cell/>
          <table:table-cell office:value-type="float" office:value="2">
            <text:p>2</text:p>
          </table:table-cell>
          <table:table-cell office:value-type="float" office:value="5.6">
            <text:p>5,6</text:p>
          </table:table-cell>
          <table:table-cell office:value-type="string">
            <text:p>Wechsel</text:p>
          </table:table-cell>
          <table:table-cell office:value-type="string">
            <text:p>Verschleiß</text:p>
          </table:table-cell>
          <table:table-cell table:formula="of:=SUM([.G5]-[.C6])" office:value-type="float" office:value="6">
            <text:p>6</text:p>
          </table:table-cell>
          <table:table-cell table:number-columns-repeated="3"/>
        </table:table-row>
        <table:table-row table:style-name="ro1">
          <table:table-cell office:value-type="date" office:date-value="2009-09-10">
            <text:p>10.09.09</text:p>
          </table:table-cell>
          <table:table-cell/>
          <table:table-cell office:value-type="float" office:value="6">
            <text:p>6</text:p>
          </table:table-cell>
          <table:table-cell office:value-type="string">
            <text:p>1 bis 6</text:p>
          </table:table-cell>
          <table:table-cell office:value-type="string">
            <text:p>Wechsel</text:p>
          </table:table-cell>
          <table:table-cell office:value-type="string">
            <text:p>Verschleiß/VM</text:p>
          </table:table-cell>
          <table:table-cell table:style-name="ce7" table:formula="of:=SUM([.G6]-[.C7])" office:value-type="float" office:value="0">
            <text:p>0</text:p>
          </table:table-cell>
          <table:table-cell table:number-columns-repeated="3"/>
        </table:table-row>
        <table:table-row table:style-name="ro1">
          <table:table-cell office:value-type="date" office:date-value="2010-02-15">
            <text:p>15.02.10</text:p>
          </table:table-cell>
          <table:table-cell office:value-type="float" office:value="10">
            <text:p>10</text:p>
          </table:table-cell>
          <table:table-cell table:style-name="Default"/>
          <table:table-cell table:style-name="Default"/>
          <table:table-cell office:value-type="string">
            <text:p>Eingang</text:p>
          </table:table-cell>
          <table:table-cell office:value-type="string">
            <text:p>Bestellung</text:p>
          </table:table-cell>
          <table:table-cell table:formula="of:=SUM([.G7]-[.C8]+[.B8])" office:value-type="float" office:value="10">
            <text:p>10</text:p>
          </table:table-cell>
          <table:table-cell table:number-columns-repeated="3"/>
        </table:table-row>
        <table:table-row table:style-name="ro1">
          <table:table-cell office:value-type="date" office:date-value="2010-02-16">
            <text:p>16.02.10</text:p>
          </table:table-cell>
          <table:table-cell/>
          <table:table-cell office:value-type="float" office:value="4">
            <text:p>4</text:p>
          </table:table-cell>
          <table:table-cell office:value-type="string">
            <text:p>3 bis 6</text:p>
          </table:table-cell>
          <table:table-cell office:value-type="string">
            <text:p>Wechsel</text:p>
          </table:table-cell>
          <table:table-cell office:value-type="string">
            <text:p>Verschleiß/VM</text:p>
          </table:table-cell>
          <table:table-cell table:formula="of:=SUM([.G8]-[.C9]+[.B9])" office:value-type="float" office:value="6">
            <text:p>6</text:p>
          </table:table-cell>
          <table:table-cell table:number-columns-repeated="3"/>
        </table:table-row>
        <table:table-row table:style-name="ro1">
          <table:table-cell office:value-type="date" office:date-value="2010-09-07">
            <text:p>07.09.10</text:p>
          </table:table-cell>
          <table:table-cell/>
          <table:table-cell office:value-type="float" office:value="6">
            <text:p>6</text:p>
          </table:table-cell>
          <table:table-cell office:value-type="string">
            <text:p>1 bis 6</text:p>
          </table:table-cell>
          <table:table-cell office:value-type="string">
            <text:p>Wechsel</text:p>
          </table:table-cell>
          <table:table-cell office:value-type="string">
            <text:p>Verschleiß/VM</text:p>
          </table:table-cell>
          <table:table-cell table:formula="of:=SUM([.G9]-[.C10]+[.B10])" office:value-type="float" office:value="0">
            <text:p>0</text:p>
          </table:table-cell>
          <table:table-cell table:number-columns-repeated="3"/>
        </table:table-row>
        <table:table-row table:style-name="ro1">
          <table:table-cell office:value-type="date" office:date-value="2010-11-01">
            <text:p>01.11.10</text:p>
          </table:table-cell>
          <table:table-cell office:value-type="float" office:value="10">
            <text:p>10</text:p>
          </table:table-cell>
          <table:table-cell/>
          <table:table-cell/>
          <table:table-cell office:value-type="string">
            <text:p>Eingang</text:p>
          </table:table-cell>
          <table:table-cell office:value-type="string">
            <text:p>Bestellung</text:p>
          </table:table-cell>
          <table:table-cell table:formula="of:=SUM([.G10]-[.C11]+[.B11])" office:value-type="float" office:value="10">
            <text:p>10</text:p>
          </table:table-cell>
          <table:table-cell table:number-columns-repeated="3"/>
        </table:table-row>
        <table:table-row table:style-name="ro1">
          <table:table-cell office:value-type="date" office:date-value="2011-01-03">
            <text:p>03.01.11</text:p>
          </table:table-cell>
          <table:table-cell/>
          <table:table-cell office:value-type="float" office:value="2">
            <text:p>2</text:p>
          </table:table-cell>
          <table:table-cell office:value-type="float" office:value="3.4">
            <text:p>3,4</text:p>
          </table:table-cell>
          <table:table-cell office:value-type="string">
            <text:p>Wechsel</text:p>
          </table:table-cell>
          <table:table-cell office:value-type="string">
            <text:p>Verschleiß/VM</text:p>
          </table:table-cell>
          <table:table-cell table:formula="of:=SUM([.G11]-[.C12]+[.B12])" office:value-type="float" office:value="8">
            <text:p>8</text:p>
          </table:table-cell>
          <table:table-cell table:number-columns-repeated="3"/>
        </table:table-row>
        <table:table-row table:style-name="ro1">
          <table:table-cell office:value-type="date" office:date-value="2011-02-28">
            <text:p>28.02.11</text:p>
          </table:table-cell>
          <table:table-cell/>
          <table:table-cell office:value-type="float" office:value="2">
            <text:p>2</text:p>
          </table:table-cell>
          <table:table-cell office:value-type="float" office:value="5.6">
            <text:p>5,6</text:p>
          </table:table-cell>
          <table:table-cell office:value-type="string">
            <text:p>Wechsel</text:p>
          </table:table-cell>
          <table:table-cell office:value-type="string">
            <text:p>Verschleiß</text:p>
          </table:table-cell>
          <table:table-cell table:formula="of:=SUM([.G12]-[.C13]+[.B13])" office:value-type="float" office:value="6">
            <text:p>6</text:p>
          </table:table-cell>
          <table:table-cell table:number-columns-repeated="3"/>
        </table:table-row>
        <table:table-row table:style-name="ro1">
          <table:table-cell office:value-type="date" office:date-value="2011-03-24">
            <text:p>24.03.11</text:p>
          </table:table-cell>
          <table:table-cell/>
          <table:table-cell office:value-type="float" office:value="2">
            <text:p>2</text:p>
          </table:table-cell>
          <table:table-cell office:value-type="float" office:value="1.2">
            <text:p>1,2</text:p>
          </table:table-cell>
          <table:table-cell office:value-type="string">
            <text:p>Wechsel</text:p>
          </table:table-cell>
          <table:table-cell office:value-type="string">
            <text:p>Verschleiß/Querschl.</text:p>
          </table:table-cell>
          <table:table-cell table:formula="of:=SUM([.G13]-[.C14]+[.B14])" office:value-type="float" office:value="4">
            <text:p>4</text:p>
          </table:table-cell>
          <table:table-cell table:number-columns-repeated="3"/>
        </table:table-row>
      </table:table>
      <table:table table:name="Tabelle2" table:style-name="ta1" table:print="false">
        <table:table-column table:style-name="co4" table:default-cell-style-name="Default"/>
        <table:table-row table:style-name="ro1">
          <table:table-cell/>
        </table:table-row>
      </table:table>
      <table:table table:name="Tabelle3" table:style-name="ta1" table:print="false">
        <table:table-column table:style-name="co4" table:default-cell-style-name="Default"/>
        <table:table-row table:style-name="ro1">
          <table:table-cell/>
        </table:table-row>
      </table:table>
    </office:spreadsheet>
  </office:body>
</office:document-content>
  </xsl:template>
</xsl:stylesheet>