<?xml version="1.0" encoding="UTF-8"?>
<anychart>
	<charts>
		<chart plot_type="CategorizedVertical">
			<chart_settings>
				<title>
					<text>${firstName} ${lastName}
					</text>
				</title>
				<legend enabled="true">
					<title enabled="false" />
					<icon>
						<marker enabled="true" type="%MarkerType" size="8" />
					</icon>
				</legend>
				<axes>
					<x_axis tickmarks_placement="Center">
						<title>
							<text><![CDATA[Month]]></text>
						</title>
					</x_axis>
					<y_axis>
						<title>
							<text><![CDATA[Value]]></text>
						</title>
						<labels>
							<format><![CDATA[{%Value}{numDecimals:0}]]></format>
						</labels>
						<minor_grid enabled="false" />
						<major_grid interlaced="false" />
						<minor_tickmark enabled="false" />
					</y_axis>
				</axes>
			</chart_settings>
			<data_plot_settings default_series_type="Line">
				<line_series>
					<line_style>
						<line dashed="true" dash_length="5" space_length="5" />
					</line_style>
					<effects enabled="false" />
				</line_series>
			</data_plot_settings>
			<data>
				<series name="Series 1">
					<#list averages as avg>
					<point name="${avg[0]}" y="${avg[1]}" />
					</#list>
					</series>
					<series name="Bubble" type="Bubble">
					<point name="${back}" y="${backValue}" size="1">
					<attributes>
					<attribute name="var">${back}</attribute>
					</attributes>
					</point>
					</series>
			</data>
		</chart>
	</charts>
</anychart>