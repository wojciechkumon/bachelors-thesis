Linux only!
Library needed: Pistache


http://pistache.io/quickstart

git clone https://github.com/oktal/pistache.git
git submodule update --init

cd pistache
mkdir build
cd build
cmake -G "Unix Makefiles" -DCMAKE_BUILD_TYPE=Release ..
make
sudo make install
