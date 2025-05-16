<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:output method="html" encoding="UTF-8" indent="yes"/>

  <!-- Plantilla raíz -->
  <xsl:template match="/Medicines">
    <html>
      <head>
        <title>List of medicines</title>
        <style>
          body {font-family: "Times New Roman", Times, serif; background-image: url('../ImageFlowers/imagenprado.png'); background-repeat: no-repeat; background-size: cover; background-position: center top; color: black; height: 100vh; margin: 0; display: flex; justify-content: center; align-items: center; }. content{text-align: center;}        
          table { border-collapse: collapse; width: 70%; margin: 0 auto; font-family: "Times New Roman", Times, serif; position: absolute; top: 100px; left: 50%; transform: translateX(-50%);}
          th, td { border: 1px solid black; padding: 8px; text-align: left; background-color: rgba(224, 247, 250, 0.7);}
          th { background-color: rgba(224, 247, 250, 0.7); }
          h2 {margin-bottom: 20px; color: black; background-color: rgba(255, 255, 255, 0.6); padding: 10px; border-radius: 10px; display: inline-block; position: absolute; top: 10px; left: 50%; transform: translateX(-50%);}
        </style>
      </head>
      <body>
        <h2>Medicines in the supply</h2>
        <table>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Quantity</th>
          </tr>
          <xsl:for-each select="Medicine">
            <tr>
              <td><xsl:value-of select="id"/></td>
              <td><xsl:value-of select="name"/></td>
              <td><xsl:value-of select="quantity"/></td>
            </tr>
          </xsl:for-each>
        </table>
      </body>
    </html>
  </xsl:template>

</xsl:stylesheet>