<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" indent="yes" encoding="UTF-8"/>
    <xsl:variable name="targetSkill" select="/data/user[1]/cookingSkillLevel"/>
    <xsl:variable name="userName" select="/data/user[1]/name"/>

    <xsl:template match="/">
        <html>
            <head>
                <title>Styled Recipe Catalog</title>
                <style>
                    body { font-family: 'Segoe UI', Arial, sans-serif; margin: 40px; background: #f8f9fa; }
                    .header-panel { background: #6f50cb; color: white; padding: 30px; border-radius: 15px; margin-bottom: 30px; box-shadow: 0 4px 15px rgba(0,0,0,0.1); }
                    table { border-collapse: collapse; width: 100%; background: white; border-radius: 10px; overflow: hidden; box-shadow: 0 2px 10px rgba(0,0,0,0.05); }
                    th, td { padding: 15px; text-align: left; border-bottom: 1px solid #eee; }
                    th { background: #f2f2f2; font-weight: bold; text-transform: uppercase; font-size: 0.8rem; }
                    .match { background-color: #fff9c4 !important; font-weight: bold; }
                    .no-match { background-color: #d4edda !important; }
                    .legend { margin-top: 20px; font-size: 0.9rem; }
                    .dot { height: 10px; width: 10px; border-radius: 50%; display: inline-block; margin-right: 5px; }
                </style>
            </head>
            <body>
                <div class="header-panel">
                    <h1>Personalized Recipe Catalog</h1>
                    <p>Welcome back, <strong><xsl:value-of select="$userName"/></strong>!</p>
                    <p>Current Skill Level: <strong><xsl:value-of select="$targetSkill"/></strong></p>
                </div>

                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Title</th>
                            <th>Cuisines</th>
                            <th>Difficulty</th>
                        </tr>
                    </thead>
                    <tbody>
                        <xsl:for-each select="/data/recipes/recipe">
                            <tr>
                                <xsl:attribute name="class">
                                    <xsl:choose>
                                        <xsl:when test="difficulty = $targetSkill">match</xsl:when>
                                        <xsl:otherwise>no-match</xsl:otherwise>
                                    </xsl:choose>
                                </xsl:attribute>
                                <td><xsl:value-of select="@id"/></td>
                                <td><xsl:value-of select="title"/></td>
                                <td>
                                    <xsl:for-each select="cuisines/cuisine">
                                        <xsl:value-of select="."/>
                                        <xsl:if test="position() != last()">, </xsl:if>
                                    </xsl:for-each>
                                </td>
                                <td><xsl:value-of select="difficulty"/></td>
                            </tr>
                        </xsl:for-each>
                    </tbody>
                </table>

                <div class="legend">
                    <span class="dot" style="background:#fff9c4"></span> Matches your skill (Yellow) |
                    <span class="dot" style="background:#d4edda"></span> Other levels (Green)
                </div>
                <p><a href="/index.html" style="color:#6f50cb; text-decoration:none; font-weight:bold;">← Back to Dashboard</a></p>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>