#include <assert.h>
#include <elf.h>
#include <fcntl.h>
#include <fstream>
#include <getopt.h>
#include <iomanip>
#include <iostream>
#include <memory>
#include <sstream>
#include <string.h>
#include <string>
#include <sys/stat.h>
#include <sys/types.h>
#include <unistd.h>
#include <vector>

#define Assert(cond, fmt, ...)                            \
  do {                                                    \
    if (!(cond)) {                                        \
      fprintf(stderr, "error: " fmt "\n", ##__VA_ARGS__); \
      exit(1);                                            \
    }                                                     \
  } while (0)

struct AddrSpace {
  std::string coe;
  unsigned addr;
  unsigned size;
  std::vector<char> storage;
};

template <typename Out>
void split(const std::string &s, char delim, Out result) {
  std::istringstream iss(s);
  std::string item;
  while (std::getline(iss, item, delim)) {
    *result++ = item;
  }
}

std::vector<std::string> split(
    const std::string &s, char delim) {
  std::vector<std::string> elems;
  split(s, delim, std::back_inserter(elems));
  return elems;
}

size_t get_file_size(const char *img_file) {
  struct stat file_status;
  lstat(img_file, &file_status);
  if (S_ISLNK(file_status.st_mode)) {
    char *buf = (char *)malloc(file_status.st_size + 1);
    size_t size =
        readlink(img_file, buf, file_status.st_size);
    (void)size;
    buf[file_status.st_size] = 0;
    size = get_file_size(buf);
    free(buf);
    return size;
  } else {
    return file_status.st_size;
  }
}

uint8_t *read_file(const char *filename) {
  size_t size = get_file_size(filename);
  int fd = open(filename, O_RDONLY);
  if (fd == -1) return NULL;

  // malloc buf which should be freed by caller
  uint8_t *buf = (uint8_t *)malloc(size);
  int len = 0;
  while (len < size) { len += read(fd, buf, size - len); }
  close(fd);
  return buf;
}

char *as_map(std::vector<AddrSpace> &space, uint32_t addr,
    uint32_t size) {
  for (auto &as : space) {
    if (as.addr <= addr && as.addr + as.size >= addr + size)
      return &as.storage.at(addr - as.addr);
  }
  return nullptr;
}

void as_load(
    std::vector<AddrSpace> &space, const char *elf_file) {
  uint8_t *buf = read_file(elf_file);
  Assert(buf, "elf file cannot be opened for reading");

  Elf32_Ehdr *elf = (Elf32_Ehdr *)buf;
  Assert(*(uint32_t *)elf == 0x464c457f,
      "specified elf file has bad magic number");
  for (int i = 0; i < elf->e_phnum; i++) {
    Elf32_Phdr *ph =
        (Elf32_Phdr *)(buf + i * elf->e_phentsize +
                       elf->e_phoff);
    if (ph->p_type != PT_LOAD) { continue; }

    char *ptr = as_map(space, ph->p_vaddr, ph->p_memsz);
    Assert(ptr,
        "elf file has sections out of specified range");
    memcpy(ptr, buf + ph->p_offset, ph->p_filesz);
    memset(
        ptr + ph->p_filesz, 0, ph->p_memsz - ph->p_filesz);
  }

  if (elf->e_entry != 0xbfc00000) {
    uint32_t entry = elf->e_entry;
    uint32_t *p =
        (uint32_t *)as_map(space, 0xbfc00000, 4 * 4);
    Assert(p, "addr range 0xBFC00000 is not specified");
    p[0] = 0x3c080000 | (entry >> 16); // lui t0, %hi(entry)
    p[1] = 0x35080000 |
           (entry & 0xFFFF); // ori t0, t0, %lo(entry)
    p[2] = 0x01000008;       // jr t0
    p[3] = 0x00000000;       // nop
  }

  free(buf);
}

void as_realize(const std::vector<AddrSpace> &space) {
  for (auto &as : space) {
    std::ofstream ofs(as.coe);
    ofs << "memory_initialization_radix = 16;\n";
    ofs << "memory_initialization_vector =\n";

    uint32_t *ptr = (uint32_t *)&as.storage[0];
    unsigned nr_words = as.storage.size() / 4;
    unsigned last_nonzero = 1;
    for (int i = nr_words - 1; i >= 0; i--) {
      if (ptr[i] != 0) {
        last_nonzero = std::min((int)nr_words, i + 4);
        break;
      }
    }

    for (int i = 0; i < last_nonzero; i++) {
      ofs << std::hex << std::setw(8) << std::setfill('0')
          << ptr[i] << "\n";
    }
  }
}

int main(int argc, char *const argv[]) {
  const char *elf_file = nullptr;
  std::vector<AddrSpace> space;
  for (int i = 1; i < argc; i++) {
    if (strncmp(argv[i], "-e", 2) == 0) {
      elf_file = argv[++i];
    } else if (strncmp(argv[i], "-s", 2) == 0) {
      const char *optarg = argv[++i];
      std::vector<std::string> pieces = split(optarg, ':');
      Assert(
          pieces.size() == 3, "wrong format in -s option");

      AddrSpace as;
      as.coe = pieces[0];
      as.addr = stoll(pieces[1], nullptr, 16);
      as.size = stoll(pieces[2], nullptr, 10);
      as.storage.resize(as.size);
      space.push_back(std::move(as));
    }
  }

  Assert(elf_file, "elf file is not specified");
  as_load(space, elf_file);
  as_realize(space);
  return 0;
}
