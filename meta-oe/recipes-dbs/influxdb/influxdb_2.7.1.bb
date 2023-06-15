DESCRIPTION = "InfluxDB is a time series database designed to handle high write and query loads."
HOMEPAGE = "https://www.influxdata.com/products/influxdb-overview/"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://src/${GO_IMPORT}/LICENSE;md5=86f93342918318318c795b9999bd8131"

RDEPENDS:${PN} = "bash"
RDEPENDS:${PN}-dev = "bash"

GO_IMPORT = "github.com/influxdata/influxdb"

GO_INSTALL = "\
    ${GO_IMPORT}/cmd/influx \
    ${GO_IMPORT}/cmd/influxd \
"

SRC_URI = "\
    git://${GO_IMPORT};protocol=https;branch=2.7;destsuffix=${BPN}-${PV}/src/${GO_IMPORT} \
    file://influxdb \
    file://influxdb.conf \
"

SRC_URI:append:mipsarch = " file://0001-patch-term-module-for-mips-ispeed-ospeed-termios-abs.patch;patchdir=src/${GO_IMPORT}"

SRCREV = "407fa622e9a0a48516dacc7564f7ba59c8307da9"

CGO_ENABLED = "1"

inherit go-mod pkgconfig systemd update-rc.d useradd

# Workaround for network access issue during compile step
# this needs to be fixed in the recipes buildsystem to move
# this such that it can be accomplished during do_fetch task
do_compile[network] = "1"

USERADD_PACKAGES = "${PN}"
USERADD_PARAM:${PN} = "--system -d /var/lib/influxdb -m -s /bin/nologin influxdb"

do_install:prepend() {
    rm ${B}/src/${GO_IMPORT}/build.py
    rm ${B}/src/${GO_IMPORT}/build.sh
    rm ${B}/src/${GO_IMPORT}/Dockerfile*
    sed -i -e "s#usr/bin/sh#bin/sh#g" ${B}/src/${GO_IMPORT}/scripts/ci/run_perftest.sh
}

do_install:append() {
    install -d ${D}${sysconfdir}/influxdb
    install -m 0644 ${WORKDIR}/influxdb.conf ${D}${sysconfdir}/influxdb
    chown -R root.influxdb ${D}${sysconfdir}/influxdb

    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/influxdb ${D}${sysconfdir}/init.d/influxdb

    if [ "${@bb.utils.filter('DISTRO_FEATURES', 'sysvinit', d)}" ] ; then
        install -d ${D}${sysconfdir}/logrotate.d
        install -m 0644 ${S}/src/${GO_IMPORT}/scripts/logrotate ${D}${sysconfdir}/logrotate.d/influxdb
    fi

    if [ "${@bb.utils.filter('DISTRO_FEATURES', 'systemd', d)}" ] ; then
        install -d ${D}${systemd_unitdir}/system
        install -m 0644 ${S}/src/${GO_IMPORT}/scripts/influxdb.service ${D}${systemd_system_unitdir}/influxdb.service
    fi

    # TODO chown
}

INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME = "influxdb"
INITSCRIPT_PARAMS = "defaults"

SYSTEMD_SERVICE:${PN} = "influxdb.service"
