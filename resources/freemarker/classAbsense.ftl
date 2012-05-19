<?xml version="1.0" encoding="UTF-8"?>
<anychart>
	<settings>
		<animation enabled="True" />
	</settings>
	<charts>
		<chart plot_type="CategorizedHorizontal">
			<data_plot_settings default_series_type="Bar">
				<bar_series point_padding="0.2" group_padding="1" style="AquaLight">
					<bar_style>
						<fill opacity="1" />
						<states>
							<hover color="White" />
						</states>
					</bar_style>
					<tooltip_settings enabled="True" />
				</bar_series>
			</data_plot_settings>
			<chart_settings>
				<title enabled="true">
					<text>${chartName}</text>
				</title>
				<axes>
					<y_axis position="Opposite">
						<title>
							<text>${yAxis}</text>
						</title>
					</y_axis>
					<x_axis>
						<title>
							<text>${xAxis}</text>
						</title>
					</x_axis>
				</axes>
			</chart_settings>
			<data>
				<series name="Series 1">
					<#list chartProperties as cp>
					<point name="${cp[0]}" y="${cp[1]}">
						<attributes>
							<attribute name="fullName">${cp[2]}</attribute>
							<attribute name="methodName">${methodName}</attribute>
						</attributes>
					</point>
					</#list>
				</series>
			</data>
		</chart>
	</charts>
</anychart>