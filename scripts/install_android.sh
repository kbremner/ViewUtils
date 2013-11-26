#!/bin/sh
#
# This script installs the android dependency into maven. Borrowed from Robolectrics
#
# Usage:
#   install_android.sh
#
# Assumptions:
#  1. You've got one or more Android SDKs installed locally.
#  2. Your ANDROID_HOME environment variable points to the Android SDK install dir.

echo "Installing com.google.android:android from $ANDROID_HOME/platforms"
mvn -q install:install-file -DgroupId=com.google.android -DartifactId=android \
  -Dversion=18 -Dpackaging=jar -Dfile="$ANDROID_HOME/platforms/android-18/android.jar"

echo "Done!"