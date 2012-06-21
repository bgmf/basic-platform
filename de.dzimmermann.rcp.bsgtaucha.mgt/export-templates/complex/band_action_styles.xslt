<?xml version="1.0" encoding="UTF-8"?>
<office:document-styles xmlns:office="urn:oasis:names:tc:opendocument:xmlns:office:1.0" xmlns:style="urn:oasis:names:tc:opendocument:xmlns:style:1.0" xmlns:text="urn:oasis:names:tc:opendocument:xmlns:text:1.0" xmlns:table="urn:oasis:names:tc:opendocument:xmlns:table:1.0" xmlns:draw="urn:oasis:names:tc:opendocument:xmlns:drawing:1.0" xmlns:fo="urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:meta="urn:oasis:names:tc:opendocument:xmlns:meta:1.0" xmlns:number="urn:oasis:names:tc:opendocument:xmlns:datastyle:1.0" xmlns:presentation="urn:oasis:names:tc:opendocument:xmlns:presentation:1.0" xmlns:svg="urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0" xmlns:chart="urn:oasis:names:tc:opendocument:xmlns:chart:1.0" xmlns:dr3d="urn:oasis:names:tc:opendocument:xmlns:dr3d:1.0" xmlns:math="http://www.w3.org/1998/Math/MathML" xmlns:form="urn:oasis:names:tc:opendocument:xmlns:form:1.0" xmlns:script="urn:oasis:names:tc:opendocument:xmlns:script:1.0" xmlns:ooo="http://openoffice.org/2004/office" xmlns:ooow="http://openoffice.org/2004/writer" xmlns:oooc="http://openoffice.org/2004/calc" xmlns:dom="http://www.w3.org/2001/xml-events" xmlns:rpt="http://openoffice.org/2005/report" xmlns:of="urn:oasis:names:tc:opendocument:xmlns:of:1.2" xmlns:xhtml="http://www.w3.org/1999/xhtml" xmlns:grddl="http://www.w3.org/2003/g/data-view#" xmlns:tableooo="http://openoffice.org/2009/table" xmlns:css3t="http://www.w3.org/TR/css3-text/" office:version="1.2" grddl:transformation="http://docs.oasis-open.org/office/1.2/xslt/odf2rdf.xsl">
  <office:font-face-decls>
    <style:font-face style:name="Arial" svg:font-family="Arial" style:font-family-generic="swiss" style:font-pitch="variable"/>
    <style:font-face style:name="DejaVu Sans" svg:font-family="'DejaVu Sans'" style:font-family-generic="system" style:font-pitch="variable"/>
    <style:font-face style:name="Lohit Hindi" svg:font-family="'Lohit Hindi'" style:font-family-generic="system" style:font-pitch="variable"/>
    <style:font-face style:name="Mangal" svg:font-family="Mangal" style:font-family-generic="system" style:font-pitch="variable"/>
    <style:font-face style:name="Microsoft YaHei" svg:font-family="'Microsoft YaHei'" style:font-family-generic="system" style:font-pitch="variable"/>
  </office:font-face-decls>
  <office:styles>
    <style:default-style style:family="table-cell">
      <style:paragraph-properties style:tab-stop-distance="1.25cm"/>
      <style:text-properties style:font-name="Arial" fo:language="de" fo:country="DE" style:font-name-asian="DejaVu Sans" style:language-asian="zh" style:country-asian="CN" style:font-name-complex="DejaVu Sans" style:language-complex="hi" style:country-complex="IN"/>
    </style:default-style>
    <number:number-style style:name="N0">
      <number:number number:min-integer-digits="1"/>
    </number:number-style>
    <number:currency-style style:name="N106P0" style:volatile="true">
      <number:number number:decimal-places="2" number:min-integer-digits="1" number:grouping="true"/>
      <number:text> </number:text>
      <number:currency-symbol number:language="de" number:country="DE">€</number:currency-symbol>
    </number:currency-style>
    <number:currency-style style:name="N106">
      <style:text-properties fo:color="#ff0000"/>
      <number:text>-</number:text>
      <number:number number:decimal-places="2" number:min-integer-digits="1" number:grouping="true"/>
      <number:text> </number:text>
      <number:currency-symbol number:language="de" number:country="DE">€</number:currency-symbol>
      <style:map style:condition="value()&gt;=0" style:apply-style-name="N106P0"/>
    </number:currency-style>
    <number:currency-style style:name="N107P0" style:volatile="true">
      <number:number number:decimal-places="2" number:min-integer-digits="0" number:grouping="true"/>
      <number:text> </number:text>
      <number:currency-symbol number:language="de" number:country="DE">€</number:currency-symbol>
    </number:currency-style>
    <number:currency-style style:name="N107">
      <style:text-properties fo:color="#ff0000"/>
      <number:text>-</number:text>
      <number:number number:decimal-places="2" number:min-integer-digits="0" number:grouping="true"/>
      <number:text> </number:text>
      <number:currency-symbol number:language="de" number:country="DE">€</number:currency-symbol>
      <style:map style:condition="value()&gt;=0" style:apply-style-name="N107P0"/>
    </number:currency-style>
    <number:number-style style:name="N108">
      <number:number number:decimal-places="0" number:min-integer-digits="0"/>
    </number:number-style>
    <style:style style:name="Default" style:family="table-cell">
      <style:text-properties style:font-name-complex="Lohit Hindi"/>
    </style:style>
    <style:style style:name="Result" style:family="table-cell" style:parent-style-name="Default">
      <style:text-properties fo:font-style="italic" style:text-underline-style="solid" style:text-underline-width="auto" style:text-underline-color="font-color" fo:font-weight="bold"/>
    </style:style>
    <style:style style:name="Result2" style:family="table-cell" style:parent-style-name="Result" style:data-style-name="N106"/>
    <style:style style:name="Heading" style:family="table-cell" style:parent-style-name="Default">
      <style:table-cell-properties style:text-align-source="fix" style:repeat-content="false"/>
      <style:paragraph-properties fo:text-align="center"/>
      <style:text-properties fo:font-size="16pt" fo:font-style="italic" fo:font-weight="bold"/>
    </style:style>
    <style:style style:name="Heading1" style:family="table-cell" style:parent-style-name="Heading">
      <style:table-cell-properties style:rotation-angle="90"/>
    </style:style>
  </office:styles>
  <office:automatic-styles>
    <style:page-layout style:name="Mpm1">
      <style:page-layout-properties fo:page-width="29.7cm" fo:page-height="21.001cm" style:num-format="1" style:print-orientation="landscape" style:writing-mode="lr-tb"/>
      <style:header-style>
        <style:header-footer-properties fo:min-height="0.75cm" fo:margin-left="0cm" fo:margin-right="0cm" fo:margin-bottom="0.25cm"/>
      </style:header-style>
      <style:footer-style>
        <style:header-footer-properties fo:min-height="0.751cm" fo:margin-left="0cm" fo:margin-right="0cm" fo:margin-top="0.25cm"/>
      </style:footer-style>
    </style:page-layout>
    <style:page-layout style:name="Mpm2">
      <style:page-layout-properties style:writing-mode="lr-tb"/>
      <style:header-style>
        <style:header-footer-properties fo:min-height="0.751cm" fo:margin-left="0cm" fo:margin-right="0cm" fo:margin-bottom="0.25cm" fo:border="0.088cm solid #000000" fo:padding="0.018cm" fo:background-color="#c0c0c0">
          <style:background-image/>
        </style:header-footer-properties>
      </style:header-style>
      <style:footer-style>
        <style:header-footer-properties fo:min-height="0.751cm" fo:margin-left="0cm" fo:margin-right="0cm" fo:margin-top="0.25cm" fo:border="0.088cm solid #000000" fo:padding="0.018cm" fo:background-color="#c0c0c0">
          <style:background-image/>
        </style:header-footer-properties>
      </style:footer-style>
    </style:page-layout>
  </office:automatic-styles>
  <office:master-styles>
    <style:master-page style:name="Default" style:page-layout-name="Mpm1">
      <style:header>
        <text:p>
          <text:sheet-name>???</text:sheet-name>
        </text:p>
      </style:header>
      <style:header-left style:display="false"/>
      <style:footer>
        <text:p>Seite <text:page-number>1</text:page-number></text:p>
      </style:footer>
      <style:footer-left style:display="false"/>
    </style:master-page>
    <style:master-page style:name="Report" style:page-layout-name="Mpm2">
      <style:header>
        <style:region-left>
          <text:p><text:sheet-name>???</text:sheet-name> (<text:title>???</text:title>)</text:p>
        </style:region-left>
        <style:region-right>
          <text:p><text:date style:data-style-name="N2" text:date-value="2011-05-24">24.05.2011</text:date>, <text:time>10:03:10</text:time></text:p>
        </style:region-right>
      </style:header>
      <style:header-left style:display="false"/>
      <style:footer>
        <text:p>Seite <text:page-number>1</text:page-number> / <text:page-count>99</text:page-count></text:p>
      </style:footer>
      <style:footer-left style:display="false"/>
    </style:master-page>
  </office:master-styles>
</office:document-styles>
