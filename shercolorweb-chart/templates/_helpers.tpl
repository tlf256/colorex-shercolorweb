{{/*
This file is used to provide helm template helper functions to the rest of the templates in the chart.
*/}}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "shercolorweb.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "shercolorweb.labels" -}}
helm.sh/chart: {{ include "shercolorweb.chart" . }}
{{ include "shercolorweb.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "shercolorweb.selectorLabels" -}}
app.kubernetes.io/name: {{ .Chart.Name }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}